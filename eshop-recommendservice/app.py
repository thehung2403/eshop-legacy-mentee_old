from flask import Flask
import requests, random, os

_url_productservice = os.environ.get("URL_PRODUCTSERVICE", default="http://localhost:8080")

app = Flask(__name__)

@app.route("/api/recommends", methods=['GET'])
def recommend():
    response = requests.get(_url_productservice + "/api/products")
    products = response.json()

    recommendations = {'recommendations': random.sample(products['products'], 4)}
    print(f"recommendations: f{recommendations}")
    return recommendations
    