import asyncio, sys
from deriv_api import DerivAPI


APP_ID = "80707" # <--- IMPORTANT: REPLACE THIS WITH YOUR ACTUAL APP ID
SYMBOL = "R_100"


async def main():
    api = DerivAPI(endpoint='wss://ws.derivws.com/websockets/v3', app_id=APP_ID)
    ticks = await api.ticks(SYMBOL)
    
    def on_update(data):
        sym = data['instrument']
        price = float(data['quote'])
        epoch = int(data['epoch'])
        sys.stdout.write(f"{sym},{epoch},{price}\n")
        sys.stdout.flush()
        
        ticks.on_update().subscribe(on_update)
    await asyncio.Event().wait()

if __name__ == "__main__":
    asyncio.run(main())