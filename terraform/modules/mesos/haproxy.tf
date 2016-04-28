# Create an entry point for HAProxy to expose external applications 
# port 80 is Mapped to port 31300 and Haproxy container must be mapped on that port to work
# Haproxy 9090 is instead exposed through 31301

resource "aws_elb" "haproxy-elb" {
  name = "haproxy-elb"
  internal = "false"
  subnets = ["${aws_subnet.mesos_slaves_public.id}"]
  security_groups = ["${aws_security_group.slaves_public.id}"]

  listener {
    instance_port = 31300
    instance_protocol = "http"
    lb_port = 80
    lb_protocol = "http"
  }

  health_check {
    healthy_threshold = 2
    unhealthy_threshold = 2
    timeout = 3
    target = "HTTP:31301/_haproxy_health_check"
    interval = 30
  }
  connection_draining = true
  connection_draining_timeout = 400

  tags {
    Name = "haproxy-elb"
    Group = "${var.tag_value}"
  }
}
