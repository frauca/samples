from pydantic import BaseModel
import yaml


class FairsSettings(BaseModel):
    database_url: str = "postgresql://fairs:fairs@localhost/fairs"
    logging_conf: str = "logging.conf"
    workers: int = 1

    @staticmethod
    def from_yaml(yaml_path: str) -> "FairsSettings":
        with open(yaml_path, "r") as yaml_file:
            config_dict = yaml.safe_load(yaml_file)
            return FairsSettings(**config_dict)
