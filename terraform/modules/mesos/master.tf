## First create a stable point to call master

resource "aws_elb" "master-elb" {
  name = "master-elb"
  internal = "true"
  subnets = ["${aws_subnet.master.id}"]
  security_groups = ["${aws_security_group.master.id}"]

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
  instances = ["${aws_instance.master.id}"]
  connection_draining = true
  connection_draining_timeout = 400

  tags {
    Name = "mesos-master-elb"
  }
}

resource "aws_instance" "master" {
    ami = "ami-d7d35fa4"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.master.id}"]
    subnet_id = "${aws_subnet.master.id}"
    associate_public_ip_address = "true"
    source_dest_check = "false"
    tags {
        Name = "Mesos Master"
    }
}

resource "aws_route_table_association" "master-routing" {
    subnet_id = "${aws_subnet.master.id}"
    route_table_id = "${aws_route_table.r.id}"
}

output "mesos-console" {
    value = "http://${aws_instance.master.public_dns}:5050"
}


