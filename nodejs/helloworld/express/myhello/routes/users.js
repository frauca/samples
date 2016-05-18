var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

// handler for the /user/:id path, which prints the user ID
router.get('/:id', function (req, res) {
  res.send(req.params.id);
});

module.exports = router;
