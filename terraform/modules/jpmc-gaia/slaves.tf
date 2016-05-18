resource "aws_instance" "slave" {
    ami = "${var.slaves_ami}"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.slaves.id}"]
    subnet_id = "${var.aws_subnet_mesos_slaves}"
    associate_public_ip_address = "false"
    user_data = "ZOOKEEPER=${aws_instance.zookeeper.private_ip}\nMASTER=${aws_instance.master.private_ip}"
    source_dest_check = "false"
    tags {
        Name = "Mesos Slave"
        Group = "${var.tag_value}"
    }
}

