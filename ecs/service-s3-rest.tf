resource "aws_ecs_service" "services-s3-rest" {
  name = "services-s3-rest"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-s3-rest.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-service-s3.id}"
    container_name = "services-s3-rest"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "services-s3-rest" {
  family = "services-s3-rest"
  container_definitions = "${file("tasks/services-s3-rest.json")}"

}

