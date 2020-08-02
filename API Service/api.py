import pymysql
from flask import Flask
from flaskext.mysql import MySQL
from flask import jsonify
import json
from flask import flash, request
from flask_sqlalchemy import SQLAlchemy
from flask import Flask
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from flask_jsonpify import jsonify
from flask_restful import reqparse, abort, Api, Resource


db_connect = create_engine("postgres://srkgyxdipvjgwj:954cb039ec9ab5c5486105388023242e51c3a0115d7eff9d32304a4d22f56eaf@ec2-34-239-241-25.compute-1.amazonaws.com:5432/d9sva49df6upq5")

app = Flask(__name__)
api = Api(app)











@app.route('/fake/add', methods=['POST'])
def add_fake_signal_strength():
    try:
        LATITUDE = request.form['LATITUDE']
        LONGITUDE = request.form['LONGITUDE']
        LAC = request.form['LAC']
        MCC = request.form['MCC']
        MNC = request.form['MNC']
        BST_LAT = request.form['BST_LAT']
        BST_LON = request.form['BST_LON']
        SIGNAL_STRENGTH = request.form['SIGNAL_STRENGTH']
        OPERATOR_NAME = request.form['OPERATOR_NAME']
        NETWORK_SPEED_UP = request.form['NETWORK_SPEED_UP']
        NETWORK_SPEED_DOWN = request.form['NETWORK_SPEED_DOWN']
        COUNTRY_CODE = request.form['COUNTRY_CODE']
        OPERATOR_CODE = request.form['OPERATOR_CODE']
        # if LATITUDE and LONGITUDE and SIGNAL_STRENGTH and request.method == 'POST':
        if request.method == 'POST':
            sql = "INSERT INTO signal_strength_fake(LATITUDE, LONGITUDE, LAC, MCC, MNC, BST_LAT, BST_LON, SIGNAL_STRENGTH, OPERATOR_NAME, NETWORK_SPEED_UP, NETWORK_SPEED_DOWN, COUNTRY_CODE, OPERATOR_CODE) VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
            data = (LATITUDE, LONGITUDE, LAC, MCC, MNC, BST_LAT, BST_LON, SIGNAL_STRENGTH, OPERATOR_NAME, NETWORK_SPEED_UP, NETWORK_SPEED_DOWN, COUNTRY_CODE, OPERATOR_CODE,)
            conn = mysql.connect()
            cursor = conn.cursor()
            cursor.execute(sql, data)
            conn.commit()
            result = {}
            json_data = []
            result["status"] = "success"
            json_data.append(result)
            return json.dumps(json_data, indent=4, sort_keys=True, default=str)
        else:
             return 'Error while adding data'
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()
    return json.dumps(json_data, indent=4, sort_keys=True, default=str)


@app.route('/prod/add', methods=['POST'])
def add_prod_signal_strength():
    return request.data
    LONGITUDE = request.form['LONGITUDE']
    LATITUDE = request.form['LATITUDE']
    LAC = request.form['LAC']
    MCC = request.form['MCC']
    MNC = request.form['MNC']
    BST_LAT = request.form['BST_LAT']
    BST_LON = request.form['BST_LON']
    SIGNAL_STRENGTH = request.form['SIGNAL_STRENGTH']
    OPERATOR_NAME = request.form['OPERATOR_NAME']
    NETWORK_SPEED_UP = request.form['NETWORK_SPEED_UP']
    NETWORK_SPEED_DOWN = request.form['NETWORK_SPEED_DOWN']
    COUNTRY_CODE = request.form['COUNTRY_CODE']
    OPERATOR_CODE = request.form['OPERATOR_CODE']

    conn = db_connect.connect()  # Connect to database

    query = conn.execute(
    """INSERT INTO signal_strength_prod (LATITUDE,
    LONGITUDE,
    LAC,
    MCC,
    MNC,
    BST_LAT,
    BST_LON,
    SIGNAL_STRENGTH,
    OPERATOR_NAME,
    NETWORK_SPEED_UP,
    NETWORK_SPEED_DOWN,
    COUNTRY_CODE,
    OPERATOR_CODE) VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)  """,
    (LATITUDE,
    LONGITUDE,
    LAC,
    MCC,
    MNC,
    BST_LAT,
    BST_LON,
    SIGNAL_STRENGTH,
    OPERATOR_NAME,
    NETWORK_SPEED_UP,
    NETWORK_SPEED_DOWN,
    COUNTRY_CODE,
    OPERATOR_CODE,))

    return {"data":"ok"}

  
        
    

@app.route('/fake/get')
def get_fake_signal_strength():
    conn = db_connect.connect()  # Connect to database
    query = conn.execute(
    "Select * from signal_strength_fake"
    )  # This line performs query and returns the result
    return {
    "data": [
    dict(zip(tuple(str(i[0]) for i in query.cursor.description), j))
    for j in query.cursor.fetchall()
    ]
    }  # Ftches all columns

@app.route('/prod/get')
def get_prod_signal_strength():
    conn = db_connect.connect()  # Connect to database
    query = conn.execute(
    "Select * from signal_strength_prod"
    )  # This line performs query and returns the result
    return {
    "data": [
    dict(zip(tuple(str(i[0]) for i in query.cursor.description), j))
    for j in query.cursor.fetchall()
    ]
    }  # Ftches all columns



@app.route('/fake/dynamic')
def dynamic_fake_signal_strength():
    try:
        j_data = {'lat_low' : 0, 'lat_up' : 100, 'lon_low' : 0, 'lon_up' : 100 }
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        query = "SELECT * FROM signal_strength_fake WHERE  LATITUDE BETWEEN %s AND %s  AND LONGITUDE BETWEEN %s AND %s LIMIT 10000"
        data = (j_data['lat_low'], j_data['lat_up'], j_data['lon_low'], j_data['lon_up'])
        cursor.execute(query, data)
        rows = cursor.fetchall()
        resp = jsonify(rows)
        resp.status_code = 200
        return resp
    
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()

@app.route('/prod/dynamic')
def dynamic_prod_signal_strength():
    try:
        j_data = {'lat_low' : 0, 'lat_up' : 100, 'lon_low' : 0, 'lon_up' : 100 }
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        query = "SELECT * FROM signal_strength_production WHERE  LATITUDE BETWEEN %s AND %s  AND LONGITUDE BETWEEN %s AND %s LIMIT 10000"
        data = (j_data['lat_low'], j_data['lat_up'], j_data['lon_low'], j_data['lon_up'])
        cursor.execute(query, data)
        rows = cursor.fetchall()
        resp = jsonify(rows)
        resp.status_code = 200
        return resp
    
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()




if __name__ == "__main__":
    app.run()
