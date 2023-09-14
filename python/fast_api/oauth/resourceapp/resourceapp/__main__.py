import sys
from dotenv import load_dotenv

import uvicorn
from commons.api import set_port


def run_on_port(port:int):
    set_port(port)
    uvicorn.run(
        "resourceapp.app:app",
        port=port,
        reload=True
    )

def main()->None:
    load_dotenv()
    port = int(sys.argv[1])
    run_on_port(port)

if __name__ == "__main__":
    main()