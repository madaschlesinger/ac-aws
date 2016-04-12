resource "aws_route53_zone_association" "secondary" {
  zone_id = "${module.ecs.route53-zone}"
  vpc_id = "${module.mesos.mesos_vpc}"
}
