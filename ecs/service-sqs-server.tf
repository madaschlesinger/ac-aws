resource "aws_ecs_service" "services-sqs-server" {
  name = "services-sqs-server"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-sqs-server.arn}"
  desired_count = 1

}

resource "aws_ecs_task_definition" "services-sqs-server" {
  family = "services-sqs-server"
  container_definitions = "${file("tasks/services-sqs-server.json")}"

}

