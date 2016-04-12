resource "aws_instance" "mesos-slave" {
    ami = "ami-8b8c57f8"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.slaves.id}"]
    subnet_id = "${aws_subnet.mesos_slaves.id}"
    tags {
        Name = "Mesos-Slave"
    }
}

