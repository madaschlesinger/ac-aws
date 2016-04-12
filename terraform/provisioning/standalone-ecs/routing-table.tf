resource "aws_route_table_association" "a" {
    subnet_id = "${module.ecs.ecs-subnet}"
    route_table_id = "${module.ecs.nat-ecs}"
}
