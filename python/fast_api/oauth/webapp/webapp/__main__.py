import sys

import uvicorn


def run_on_port(port:int):
    uvicorn.run(
        "webapp.app:app",
        port=port,
        reload=True
    )

def main()->None:
    port = int(sys.argv[1])
    run_on_port(port)

if __name__ == "__main__":
    main()