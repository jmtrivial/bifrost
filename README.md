# Bifröst - A camera-to-speech system for Android

## Description of the project

The aim of this project is to develop an opensource series of software for smartphones dedicated to blind people, in order to offer them comodities.

Bifröst is distributed under the [GPL v3 or later license](https://www.gnu.org/copyleft/gpl.html).

## The Color-to-speech application

The first developped tool is an Android application that you can use to know the color of an object, using image processing and speech synthesis. Here are the main steps of the processings of the application:

* The user touch the screen (not yet implemented)
* The back camera takes a photo (not yet implemented)
* An image processing step is done in order to extract one (or several) dominant colors  (not yet implemented)
* The [RGB](http://en.wikipedia.org/wiki/RGB_color_model) colors are translated in [HSL](http://en.wikipedia.org/wiki/HSL_and_HSV) space  (not yet implemented)
* The HSL colors are translated in [fuzzy](https://www.gnu.org/copyleft/gpl.html) colors (implemented)
* The fuzzy colors are translated in semantic colors (implemented)
* The semantic colors are prononced by the smartphone  (not yet implemented)

We motivate this project by the following points
* opensource software (several closed source software are existing in this domain, and usually expensive)
* ready for internationalization (the semantic colors are defined for each language)
* easily tunable (the RGB to semantic system does not use lookup tables, but logic rules that is a human friendly way to do it)

More details about the recent developments of the color-to-speech application on the dedicated webpage: [Color-to-Speech details](website/ColorToSpeechDetails.md)


## Alternative applications: bifröst and bifröst-gears

Bifrost will be distributed under two different applications:
* *Bifrost*, the full application with the camera to speech pipeline
* *Bifrost gears*, an application that let you select an HSL color (color to speech)

## Future tools

* luminosity-to-sound: using the camera, traduce the intensity of luminosity by generating high to low pitch sound 
