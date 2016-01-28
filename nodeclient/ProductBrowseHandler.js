var async = require('async');
var request = require('request');
exports.handler = function(req, res) {
  async.parallel([
    /*
     * First external endpoint
     */
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:8080/product/product-"+id;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        console.log(body);
        callback(false, obj);
      });
    },
    /*
     * Second external endpoint
     */
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:9090/price/price-"+id;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        console.log(body);
        callback(false, obj);
      });
    },
  ],
  /*
   * Collate results
   */
  function(err, results) {
    if(err) { console.log(err); res.send(500,"Server Error"); return; }
    res.send({prodcut:results[0], price:results[1]});
  }
  );


};

exports.batchHandler = function(req, res) {
  var prodcutDetailsArray = {products:[]};

  async.parallel([
    /*
     * First external endpoint
     */
    function(callback) {
      var id = req.param('id');
      //var url = "http://localhost:8080/product/product-"+id;
      var url = "http://localhost:8080/product?type=stationary";
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        var products = JSON.parse(body);
        //console.log(products);
        var requestedCompleted = 0;
        for(var i=0 ;i<products.length;i++){
          //console.log(products[i]);
          console.log("Prodcut");
          console.log(products[i].id);
          //var id = products[i].id;
          var id = "price-22";
          async.parallel([
            function(callback) {
              console.log("Price");
              console.log(products[i].id);
              var url = "http://localhost:9090/price/"+id;
              console.log(url);
              request(url, function(err, response, body) {
                // JSON body
                console.log("Price Request Happend");
                if(err) { console.log(err); callback(true); return; }
                obj = JSON.parse(body);
                console.log(products[i]);
                prodcutDetailsArray.products.push({"product":i,"price":obj});
                console.log(body);
                requestedCompleted++;
                callback(false, obj);
              });
            },
          ],
          /*
           * Collate results
           */
          function(err, results) {
            if(err) { console.log(err); res.send(500,"Server Error"); return; }
            console.log(i+"---"+(i+1 == products.length)+"---"+products.length);
            if(requestedCompleted == products.length){
              console.log(prodcutDetailsArray);
              res.send(prodcutDetailsArray);
            }
          //  res.send({prodcut:products[i], price:results[0]});
          }
          );
        }
        callback(false, products);
      });
    },
    /*
     * Second external endpoint
     */
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:9090/price/price-"+id;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        console.log(body);
        callback(false, obj);
      });
    },
  ],
  /*
   * Collate results
   */
  function(err, results) {
    if(err) { console.log(err); res.send(500,"Server Error"); return; }
    /*console.log(prodcutDetailsArray);
    res.send(prodcutDetailsArray);*/
  }
  );


};
