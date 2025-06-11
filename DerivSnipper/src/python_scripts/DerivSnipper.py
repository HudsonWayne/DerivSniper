import asyncio, sys
from deriv_api import DerivAPI


APP_ID = "80707" # <--- IMPORTANT: REPLACE THIS WITH YOUR ACTUAL APP ID
SYMBOL = "R_100"


async def main():
    api = DerivAPI(endpoint='wss://ws.derivws.com/websockets/v3', app_id=APP_ID)
    ticks = await api.ticks(SYMBOL)