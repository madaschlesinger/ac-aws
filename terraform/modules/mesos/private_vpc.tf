resource "aws_vpc" "mesos" {
    cidr_block = "10.1.0.0/16"
    enable_dns_hostnames = true
    enable_dns_support = true
    tags {
            Name = "Terraform Mesos VPC"
            Group = "${var.tag_value}"
        }
}


resource "aws_internet_gateway" "gw" {
    vpc_id = "${aws_vpc.mesos.id}"

    tags {
        Name = "Internet Gateway Terraform Mesos"
        Group = "${var.tag_value}"
    }
}

resource "aws_route_table" "r" {
    vpc_id = "${aws_vpc.mesos.id}"
    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = "${aws_internet_gateway.gw.id}"
    }

    tags {
        Name = "Route Table"
        Group = "${var.tag_value}"
    }
}

resource "aws_route_table" "nat" {
    vpc_id = "${aws_vpc.mesos.id}"
    route {
        cidr_block = "0.0.0.0/0"
        instance_id = "${aws_instance.nat.id}"
    }

    tags {
        Name = "Route Table NAT Terraform"
        Group = "${var.tag_value}"
    }
}

resource "aws_subnet" "mesos_slaves" {
    vpc_id = "${aws_vpc.mesos.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.1.1.0/24"

    tags {
        Name = "Main_Terraform_vpc_mesos_slaves"
        Group = "${var.tag_value}"
    }
}

resource "aws_subnet" "master" {
    vpc_id = "${aws_vpc.mesos.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.1.7.0/24"

    tags {
        Name = "Main_Terraform_vpc_mesos_masters"
        Group = "${var.tag_value}"
    }
}

resource "aws_route_table_association" "jumphost-route" {
    subnet_id = "${aws_subnet.jumphosts.id}"
    route_table_id = "${aws_route_table.r.id}"
}


resource "aws_subnet" "jumphosts"  {
    vpc_id = "${aws_vpc.mesos.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.1.3.0/24"

    tags {
        Name = "JumpHosts subnet Mesos VPC"
        Group = "${var.tag_value}"
    }
}


output "mesos_vpc" {
    value = "${aws_vpc.mesos.id}"
}

output "slaves_subnet" {
    value = "${aws_subnet.mesos_slaves.id}"
}
