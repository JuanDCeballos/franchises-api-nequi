resource "aws_secretsmanager_secret" "db_password" {
  name = "franchises-db-password"
}

resource "aws_secretsmanager_secret_version" "db_password_version" {
  secret_id     = aws_secretsmanager_secret.db_password.id
  secret_string = var.db_password
}

data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

data "aws_security_group" "default" {
  name   = "default"
  vpc_id = data.aws_vpc.default.id
}

resource "aws_db_subnet_group" "franchises_db_subnet_group" {
  name       = "franchises-db-subnet-group"
  subnet_ids = data.aws_subnets.default.ids

  tags = {
    Name = "Franchises DB Subnet Group"
  }
}

resource "aws_db_parameter_group" "franchises_params" {
  name   = "franchises-nequi-params"
  family = "postgres17"

  parameter {
    name  = "rds.force_ssl"
    value = "0"
  }

  tags = {
    Name = "Franchises-DB-Params"
  }
}

resource "aws_db_instance" "franchises_db" {
  identifier            = "franchises-nequi"
  engine                = "postgres"
  engine_version        = "17.4"
  instance_class        = "db.t4g.micro"
  allocated_storage     = 20
  storage_type          = "gp2"
  max_allocated_storage = 1000
  username              = "postgres"
  password              = "var.db_password"
  parameter_group_name  = aws_db_parameter_group.franchises_params.name
  skip_final_snapshot   = true
  publicly_accessible   = true

  db_subnet_group_name   = aws_db_subnet_group.franchises_db_subnet_group.name
  vpc_security_group_ids = [data.aws_security_group.default.id]

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
  security_groups    = [data.aws_security_group.default.id]
  subnets            = data.aws_subnets.default.ids

  tags = {
    Name = "Franchise-ALB"
  }
}

resource "aws_lb_target_group" "app_tg" {
  name        = "tg-api-nequi"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.default.id
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

      environment = [
        {
          name  = "DB_HOST"
          value = aws_db_instance.franchises_db.address
        },
        {
          name  = "DB_PORT"
          value = tostring(aws_db_instance.franchises_db.port)
        },
        {
          name  = "DB_NAME"
          value = "franchises_api_db"
        },
        {
          name  = "DB_USER"
          value = aws_db_instance.franchises_db.username
        }
      ]

      secrets = [
        {
          name      = "DB_PASSWORD"
          valueFrom = aws_secretsmanager_secret.db_password.arn
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
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [data.aws_security_group.default.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app_tg.arn
    container_name   = "api-nequi-container"
    container_port   = 8080
  }

  depends_on = [aws_lb_listener.http]
}
