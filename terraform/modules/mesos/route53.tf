resource "aws_route53_zone" "mesos-dev" {
  name = "mesos-dev.internal.jpmorgan.com"
  vpc_id = "${aws_vpc.mesos.id}"
  tags {
    Environment = "mesos-dev"
    Group = "${var.tag_value}"
  }
}

resource "aws_route53_delegation_set" "main" {
    reference_name = "Internal DNS"
}

