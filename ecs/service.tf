resource "aws_ecs_service" "java-cloud" {
  name = "java-cloud"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.java-cloud.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-service.id}"
    container_name = "java-cloud"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "java-cloud" {
  family = "java-cloud"
  container_definitions = "${file("tasks/java-cloud.json")}"

}

