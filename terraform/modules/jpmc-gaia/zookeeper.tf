# Need to create an Internal Network Interface and register a record into Route53

resource "aws_instance" "zookeeper" {
    ami = "${var.zookeeper_ami}"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.shared-services.id}"]
    subnet_id = "${var.aws_subnet_shared-services}"
    tags {
        Name = "Zookeeper"
        Group = "${var.tag_value}"
    }
}

## First create a stable point to call Zookeeper 
