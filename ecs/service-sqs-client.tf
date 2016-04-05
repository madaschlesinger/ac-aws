resource "aws_ecs_service" "services-sqs-client" {
  name = "services-sqs-client"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-sqs-client.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-sqs-client.id}"
    container_name = "services-sqs-client"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "services-sqs-client" {
  family = "services-sqs-client"
  container_definitions = "${file("tasks/services-sqs-client.json")}"

}

