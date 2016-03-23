# Create a new load balancer
resource "aws_elb" "nginx" {
  name = "nginx-terraform-elb"
  subnets = ["${aws_subnet.ELBS.id}"]
  security_groups = ["${aws_security_group.ELBS.id}"]

  listener {
    instance_port = 8083
    instance_protocol = "http"
    lb_port = 80
    lb_protocol = "http"
  }

  health_check {
    healthy_threshold = 2
    unhealthy_threshold = 2
    timeout = 3
    target = "HTTP:8083/"
    interval = 30
  }

  connection_draining = true
  connection_draining_timeout = 400

  tags {
    Name = "nginx-terraform-elb"
  }
}
