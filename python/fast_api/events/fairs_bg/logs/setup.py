import logging
import logging.config


def get_logger(logger_name: str) -> logging.Logger:
    return logging.getLogger(logger_name)
