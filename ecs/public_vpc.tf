resource "aws_subnet" "ELBS" {
    vpc_id = "${aws_vpc.terraform_vpc.id}"
    availability_zone = "eu-west-1a"
    cidr_block = "10.0.5.0/24"
    map_public_ip_on_launch = "true"

    tags {
        Name = "ELBS_public_subnet"
    }
}

resource "aws_route_table_association" "elb-routing-table" {
    subnet_id = "${aws_subnet.ELBS.id}"
    route_table_id = "${aws_route_table.r.id}"
}
