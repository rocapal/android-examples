PanoramioAPP
============

PanoramioAPP is an Android application example that it downloads (from
libregeosocial API) a JSON parser with near information about pictures
taken from users. This information is in JSON format, so it's needed
parser and save in parcelable structure.

This information and pictures are shown in a smart/dynamyc list that
updates new nodes of the list when the user is looking at the last 5
nodes.

The application shows the changes in GPS location directly in the list
when the app has a "running state". If the app has a "pause state"
then, the service notify the existence of news elements through
android notification system.


Description
------------

This application includes examples of the following android structures:

* Activities
* MapActivity
* Overlays
* ListActivity: smart and dynamic lists
* States of activities
* Parcelable Objects
* Location GPS
* Android Notifications
* Toast
* Services
* AsyncTask
* Runnable
* Interfaces
* Download files
* JSON Parser


License
-------

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or (at
your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/.
