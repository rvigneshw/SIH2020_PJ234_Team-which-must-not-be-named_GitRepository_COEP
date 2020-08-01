import pymysql
from flask import Flask
from flaskext.mysql import MySQL
from flask import jsonify
import json
from flask import flash, request



app = Flask(__name__)
app.secret_key = "secret key"



mysql = MySQL()

app.config['MYSQL_DATABASE_USER'] = 'rv'
app.config['MYSQL_DATABASE_PASSWORD'] = '1234'
app.config['MYSQL_DATABASE_DB'] = 'dps'
app.config['MYSQL_DATABASE_HOST'] = '34.70.67.8'

mysql = MySQL(app)






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
        # #if request.method == 'POST':
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
        # else:
        #     return 'Error while adding user'
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()
    return json.dumps(json_data, indent=4, sort_keys=True, default=str)


@app.route('/prod/add', methods=['POST'])
def add_prod_signal_strength():
    try:
        
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
        #if LATITUDE and LONGITUDE and SIGNAL_STRENGTH and request.method == 'POST':
        if request.method == 'POST':
            sql = "INSERT INTO signal_strength_production(LONGITUDE, LATITUDE, LAC, MCC, MNC, BST_LAT, BST_LON, SIGNAL_STRENGTH, OPERATOR_NAME, NETWORK_SPEED_UP, NETWORK_SPEED_DOWN, COUNTRY_CODE, OPERATOR_CODE) VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
            #data = (LATITUDE, LONGITUDE, LAC, MCC, MNC, BST_LAT, BST_LON, SIGNAL_STRENGTH, OPERATOR_NAME, NETWORK_SPEED_UP, NETWORK_SPEED_DOWN, COUNTRY_CODE, OPERATOR_CODE,)
            data = ((20.494236,72.84483, 2, 8, 6, '9', '406170784', '4', 'quis', '1', '1', 0, 0))
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
            return 'Error while adding user'
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()
    return json.dumps(json_data, indent=4, sort_keys=True, default=str)

@app.route('/fake/get')
def get_fake_signal_strength():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM signal_strength_fake ORDER BY SIGNAL_STRENGTH")
        rows = cursor.fetchall()
        resp = jsonify(rows)
        resp.status_code = 200
        return resp
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()
@app.route('/prod/get')
def get_prod_signal_strength():
    try:
        conn = mysql.connect()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        cursor.execute("SELECT * FROM signal_strength_production ORDER BY SIGNAL_STRENGTH")
        rows = cursor.fetchall()
        resp = jsonify(rows)
        resp.status_code = 200
        return resp
    except Exception as e:
        print(e)
    finally:
        cursor.close()
        conn.close()


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
