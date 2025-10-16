resource "aws_db_instance" "franchises_db" {
  identifier     = "franchises-nequi"
  engine         = "postgres"
  engine_version = "17.4"
  instance_class = "db.t4g.micro"

  allocated_storage     = 20
  storage_type          = "gp2"
  max_allocated_storage = 1000

  username = "postgres"
  password = "var.db_password"

  vpc_security_group_ids = ["sg-0c588374af36a4940"]
  publicly_accessible    = true

  parameter_group_name = "franchises-nequi-params"

  skip_final_snapshot = true

  tags = {
    Name = "Franchises-DB"
  }
}

resource "aws_ecr_repository" "app_ecr" {
  name = "api-nequi"

  image_tag_mutability = "MUTABLE"

  encryption_configuration {
    encryption_type = "AES256"
  }

  tags = {
    Name = "Franchises-App-Repo"
  }
}

resource "aws_lb" "app_alb" {
  name               = "alb-api-nequi"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [] #PENDIENTE
  subnets            = [] #PENDIENTE

  tags = {
    Name = "Franchise-ALB"
  }
}

resource "aws_lb_target_group" "app_tg" {
  name        = "tg-api-nequi"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = "" #PENDIENTE (SIN COMILLAS)
  target_type = "ip"

  health_check {
    path = "/actuator/health"
  }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.app_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app_tg.arn
  }
}

resource "aws_ecs_cluster" "main_cluster" {
  name = "nequi"

  tags = {
    Name = "Franchises-Cluster"
  }
}

resource "aws_ecs_task_definition" "app_task" {
  family                   = "task-api-nequi"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "1024"
  memory                   = "3072"
  execution_role_arn       = "arn:aws:iam::804686432060:role/ecsTaskExecutionRole"
  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture        = "ARM64"
  }

  container_definitions = jsonencode([
    {
      name      = "api-nequi-container"
      image     = aws_ecr_repository.app_ecr.repository_url
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = "/ecs/task-api-nequi"
          "awslogs-region"        = "us-east-1"
          "awslogs-stream-prefix" = "ecs"
          "awslogs-create-group"  = "true"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "app_service" {
  name            = "servicio-api-nequi"
  cluster         = aws_ecs_cluster.main_cluster.id
  task_definition = aws_ecs_task_definition.app_task.arn
  desired_count = 1
  launch_type = "FARGATE"

  network_configuration {
    subnets = [] #PENDIENTE
    security_groups = [] #PENDIENTE
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app_tg.arn
    container_name = "api-nequi-container"
    container_port = 8080
  }

  depends_on = [aws_lb_listener.http]
}
