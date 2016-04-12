resource "aws_ecs_service" "services-jgroups-rest" {
  name = "services-jgroups-rest"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-jgroups-rest.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-service-jgroups.id}"
    container_name = "services-jgroups-rest"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "services-jgroups-rest" {
  family = "services-jgroups-rest"
  container_definitions = "${file("${path.module}/tasks/services-jgroups-rest.json")}"

}

