resource "aws_vpc_peering_connection" "peering" {
    peer_owner_id = "625998979483"
    peer_vpc_id = "${module.mesos.mesos_vpc}"
    vpc_id = "${module.ecs.terraform_vpc}"
    auto_accept = "True"
}

## Route table for ECS

resource "aws_route_table" "nat-ecs" {
    vpc_id = "${module.ecs.terraform_vpc}"
    route {
        cidr_block = "10.1.0.0/16"
        vpc_peering_connection_id = "${aws_vpc_peering_connection.peering.id}"
    }
    route {
        cidr_block = "0.0.0.0/0"
        instance_id = "${module.ecs.nat-ecs}"
    }


    tags {
        Name = "Route Table NAT Terraform ECS & Mesos"
    }
}

resource "aws_route_table_association" "composite-nat-ecs" {
    subnet_id = "${module.ecs.ecs-subnet}"
    route_table_id = "${aws_route_table.nat-ecs.id}"
}

## New Composite route Table for Mesos cluster

resource "aws_route_table" "nat-mesos" {
    vpc_id = "${module.mesos.mesos_vpc}"
    route {
        cidr_block = "10.0.0.0/16"
        vpc_peering_connection_id = "${aws_vpc_peering_connection.peering.id}"
    }
    route {
        cidr_block = "0.0.0.0/0"
        instance_id = "${module.mesos.nat-mesos}"
    }


    tags {
        Name = "Route Table NAT Terraform ECS & Mesos"
    }
}

resource "aws_route_table_association" "composite-nat-mesos" {
    subnet_id = "${module.mesos.slaves_subnet}"
    route_table_id = "${aws_route_table.nat-mesos.id}"
}

