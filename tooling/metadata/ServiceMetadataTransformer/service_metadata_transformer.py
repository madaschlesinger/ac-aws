#!/usr/bin/env python
import sys
import yaml
import json


class Service(object):
    def __init__(self, name, resources, container, environment, labels):
        self.name = name
        self.resources = resources
        self.container = container
        self.environment = environment
        self.labels = labels


class Resources(object):
    def __init__(self, cpu, memory):
        self.cpu = cpu
        self.memory = memory


class Container(object):
    def __init__(self, image, networking, privileged, port_mappings):
        self.image = image
        self.networking = networking
        self.privileged = privileged
        self.port_mappings = port_mappings


class PortMapping(object):
    def __init__(self, container_port, host_port, service_port, protocol):
        self.container_port = container_port
        self.host_port = host_port
        self.service_port = service_port
        self.protocol = protocol


class ServiceParser(object):
    @classmethod
    def parse_file(cls, input_file):
        service_yaml = yaml.load(input_file)
        service = cls._parse_service(service_yaml)
        return service

    @classmethod
    def _parse_service(cls, service_yaml):
        name = service_yaml['name']
        resources = cls._parse_resources(service_yaml['resources'])
        container = cls._parse_container(service_yaml['container'])
        environment = cls._parse_key_value_pairs(service_yaml['environment'])
        labels = cls._parse_key_value_pairs(service_yaml['labels'])
        service = Service(name, resources, container, environment, labels)
        return service

    @classmethod
    def _parse_resources(cls, resources_yaml):
        cpu = resources_yaml['cpu']
        memory = resources_yaml['memory']
        resources = Resources(cpu, memory)
        return resources

    @classmethod
    def _parse_container(cls, container_yaml):
        image = container_yaml['image']
        networking = container_yaml['networking']
        privileged = container_yaml['privileged']
        port_mappings = cls._parse_port_mappings(container_yaml['portMappings'])
        container = Container(image, networking, privileged, port_mappings)
        return container

    @classmethod
    def _parse_port_mappings(cls, port_mappings_yaml):
        port_mappings = []
        for port_mapping_yaml in port_mappings_yaml:
            container_port = port_mapping_yaml['containerPort']
            host_port = port_mapping_yaml['hostPort']
            service_port = port_mapping_yaml['servicePort']
            protocol = port_mapping_yaml['protocol']
            port_mapping = PortMapping(container_port, host_port, service_port, protocol)
            port_mappings.append(port_mapping)
        return port_mappings

    @classmethod
    def _parse_key_value_pairs(cls, pairs_yaml):
        pairs_dict = {}
        for pair_yaml in pairs_yaml:
            key = pair_yaml['key']
            value = pair_yaml['value']
            pairs_dict[key] = value
        return pairs_dict


class EcsGenerator(object):
    @classmethod
    def generate(cls, service, output_file):
        service_list = cls._transform_service(service)
        json.dump(service_list, output_file, indent=2, sort_keys=True)

    @classmethod
    def _transform_service(cls, service):
        service_list = []
        service_dict = {
            'name': service.name,
            'image': service.container.image,
            'cpu': service.resources.cpu,
            'memory': service.resources.memory,
            'portMappings': cls._transform_port_mappings(service.container.port_mappings)
        }
        service_list.append(service_dict)
        return service_list

    @classmethod
    def _transform_port_mappings(cls, port_mappings):
        port_mapping_list = []
        for port_mapping in port_mappings:
            port_mapping_dict = {
                'containerPort': port_mapping.container_port,
                'hostPort': port_mapping.host_port
            }
            port_mapping_list.append(port_mapping_dict)
        return port_mapping_list


class MarathonGenerator(object):
    @classmethod
    def generate(cls, service, output_file):
        application_dict = cls._transform_service(service)
        json.dump(application_dict, output_file, indent=2, sort_keys=True)

    @classmethod
    def _transform_service(cls, service):
        application_dict = {
            'id': service.name,
            'cpus': service.resources.cpu,
            'mem': service.resources.memory,
            'labels': service.labels,
            'container': cls._transform_container(service.container)
        }
        return application_dict

    @classmethod
    def _transform_container(cls, container):
        container_dict = {
            'type': 'DOCKER',
            'docker': {
                'image': container.image,
                'network': container.networking,
                'portMappings': cls._transform_port_mappings(container.port_mappings)
            }
        }
        return container_dict

    @classmethod
    def _transform_port_mappings(cls, port_mappings):
        port_mappings_list = []
        for port_mapping in port_mappings:
            port_mapping_dict = {
                'containerPort': port_mapping.container_port,
                'hostPort': port_mapping.host_port
            }
            port_mappings_list.append(port_mapping_dict)
        return port_mappings_list


class ServiceMetadataTransformer(object):
    @classmethod
    def transform(cls, input_filename, platform_type, output_filename):
        with open(input_filename, 'r') as input_file:
            service = ServiceParser.parse_file(input_file)
        with open(output_filename, 'w') as output_file:
            if platform_type == 'ECS':
                EcsGenerator.generate(service, output_file)
            elif platform_type == 'Marathon':
                MarathonGenerator.generate(service, output_file)
            else:
                raise ValueError("not a valid platform")

if __name__ == '__main__':
    input_filename = sys.argv[1]
    platform_type = sys.argv[2]
    output_filename = sys.argv[3]
    ServiceMetadataTransformer.transform(input_filename, platform_type, output_filename)
