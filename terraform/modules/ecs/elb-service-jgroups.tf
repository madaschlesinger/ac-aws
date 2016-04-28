# Create a new load balancer
resource "aws_elb" "java-service-jgroups" {
  name = "service-elb-jgroups"
  subnets = ["${aws_subnet.ELBS.id}"]
  security_groups = ["${aws_security_group.ELBS.id}"]

  listener {
    instance_port = 8082
    instance_protocol = "http"
    lb_port = 80
    lb_protocol = "http"
  }

  health_check {
    healthy_threshold = 2
    unhealthy_threshold = 2
    timeout = 3
    target = "HTTP:8082/health"
    interval = 30
  }

  connection_draining = true
  connection_draining_timeout = 400

  tags {
    Name = "java-service-terraform-elb"
    Group = "${var.tag_value}"
  }
}
