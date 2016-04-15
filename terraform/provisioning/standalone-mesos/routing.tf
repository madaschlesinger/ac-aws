## Route table for ECS

resource "aws_route_table" "nat-mesos" {
    vpc_id = "${module.mesos.mesos_vpc}"
    route {
        cidr_block = "0.0.0.0/0"
        instance_id = "${module.mesos.nat-mesos}"
    }


    tags {
        Name = "Route Table NAT Terraform Mesos"
    }
}


resource "aws_route_table_association" "composite-nat-mesos" {
    subnet_id = "${module.mesos.slaves_subnet}"
    route_table_id = "${aws_route_table.nat-mesos.id}"
}
