output "db_endpoint" {
  description = "RDS DB endpoint"
  value = aws_db_instance.franchises_db.endpoint
}

output "ecr_repository_url" {
  description = "URL of ECR repository for Docker"
  value = aws_ecr_repository.app_ecr.repository_url
}

output "app_url" {
  description = "Public URL"
  value = "http://${aws_lb.app_alb.dns_name}"
}