# Create a new load balancer
resource "aws_elb" "java-zeromq-server" {
  name = "service-elb-zeromq-server"
  subnets = ["${aws_subnet.ELBS.id}"]
  security_groups = ["${aws_security_group.ELBS.id}"]

  listener {
    instance_port = 8084
    instance_protocol = "tcp"
    lb_port = 8084
    lb_protocol = "tcp"
  }

  health_check {
    healthy_threshold = 2
    unhealthy_threshold = 2
    timeout = 3
    target = "TCP:8084"
    interval = 30
  }

  connection_draining = true
  connection_draining_timeout = 400

  tags {
    Name = "java-service-terraform-elb"
  }
}

resource "aws_route53_record" "zeromq-server" {
   zone_id = "${aws_route53_zone.dev.zone_id}"
   name = "zeromq-server"
   type = "CNAME"
   ttl = "300"
   records = ["${aws_elb.java-zeromq-server.dns_name}"]
}
