# Need to create an Internal Network Interface and register a record into Route53

resource "aws_instance" "zookeeper" {
    ami = "ami-225ada51"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.shared-services.id}"]
    subnet_id = "${aws_subnet.shared-services.id}"
    tags {
        Name = "Zookeeper"
        Group = "${var.tag_value}"
    }
}

resource "aws_route53_record" "zookeeper-dev" {
   zone_id = "${aws_route53_zone.mesos-dev.zone_id}"
   name = "zookeeper"
   type = "A"
   ttl = "300"
   records = ["${aws_instance.zookeeper.private_ip}"]
}

resource "aws_route_table_association" "zookeeper-routing" {
    subnet_id = "${aws_subnet.shared-services.id}"
    route_table_id = "${aws_route_table.r.id}"
}

## First create a stable point to call Zookeeper 

resource "aws_elb" "zookeeper-elb" {
  name = "zookeeper-elb"
  internal = "true"
  subnets = ["${aws_subnet.shared-services.id}"]
  security_groups = ["${aws_security_group.shared-services.id}"]

  listener {
    instance_port = 2181
    instance_protocol = "tcp"
    lb_port = 2181
    lb_protocol = "tcp"
  }

  health_check {
    healthy_threshold = 2
    unhealthy_threshold = 2
    timeout = 3
    target = "TCP:2181"
    interval = 30
  }
  instances = ["${aws_instance.zookeeper.id}"]
  connection_draining = true
  connection_draining_timeout = 400

  tags {
    Name = "zookeeper-elb"
    Group = "${var.tag_value}"
  }
}
