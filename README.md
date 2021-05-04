---
title: MultipleFileChooser
description: Browse for single/multiple files on the device.
---

# cordova-multiple-filechooser

This plugin implements a File API allowing access to single/multiple files residing on the device.

## Installation

    cordova plugin add https://github.com/amit7soni/cordova-multiple-filechooser.git
    

## API

This plugin defines a global window.MultipleFileChooser object.

Although the object is in the global scope, it is not available to applications until after the deviceready event fires.

Before calling this API make sure you have storage access permission enabled.

    window.MultipleFileChooser.select(filter, success, failure);
    
## filter (optional)
    { 
      "mime": "application/pdf", // text/plain, image/png, image/jpeg, audio/wav etc
      "multiple": true // choose single or multiple files, default value false
    } 
    
## success
    var success = function(data){
      console.log(data); // returns array of file path for all the selected files
    }
    
## failure
    var success = function(err){
      console.log(err);
    }
    
## Supported Platforms
    - Android
    
