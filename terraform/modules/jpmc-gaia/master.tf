resource "aws_instance" "master" {
    ami = "${var.master_ami}"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.master.id}"]
    subnet_id = "${var.aws_subnet_master}"
    associate_public_ip_address = "false"
    user_data = "ZOOKEEPER=${aws_instance.zookeeper.private_ip}"
    source_dest_check = "false"
    tags {
        Name = "Mesos Master"
        Group = "${var.tag_value}"
    }
}

output "mesos-console" {
    value = "http://${aws_instance.master.private_ip}:5050"
}


