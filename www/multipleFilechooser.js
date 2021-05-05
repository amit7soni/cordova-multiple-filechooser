
var exec = require('cordova/exec');

var PLUGIN_NAME = 'MultipleFileChooser';

var MultipleFileChooser = {
  select: function(filter, success, failure) {
  		if (typeof filter === 'function') {
	        failure = success;
	        success = filter;
	        filter = {};
	    }

	    exec(success, failure, PLUGIN_NAME, 'open', [ filter ]);
  }
};

module.exports = MultipleFileChooser;
