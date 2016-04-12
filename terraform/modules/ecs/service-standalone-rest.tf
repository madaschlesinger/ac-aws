resource "aws_ecs_service" "services-standalone-rest" {
  name = "services-standalone-rest"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-standalone-rest.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-service-standalone.id}"
    container_name = "services-standalone-rest"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "services-standalone-rest" {
  family = "services-standalone-rest"
  container_definitions = "${file("${path.module}/tasks/services-standalone-rest.json")}"

}

resource "aws_route53_record" "service-standalone" {
   zone_id = "${aws_route53_zone.dev.zone_id}"
   name = "service-standalone"
   type = "CNAME"
   ttl = "300"
   records = ["${aws_elb.java-service-standalone.dns_name}"]
}
