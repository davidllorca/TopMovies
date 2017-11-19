# Top Movies

[@author](https://www.linkedin.com/in/davidllorca/): David Llorca

#### Specs

Create a view that contains an infinite scroll list with the most popular tv shows. Use the following endpoint:  https://developers.themoviedb.org/3/movies/get-top-rated-movies
Each item of the list should contain an image, the tv show title and the vote average fields.
The list should paginate
If a list item is clicked, it should load the tv show data in a detail view. This should contain: A big hero image, the title, the overview... (you can get that info exploring the provided api)
Once in the detail view, the user should be able to navigate between similar tv shows                                   (https://developers.themoviedb.org/3/movies/get-similar-movies) by swapping horizontally. The first item will be the one that the user has clicked. Then it will load the related tv shows and the user will be able to navigate using swype to left or right.

#### Summary
- Architecture: MVP + Reactive Streams([RxJava](https://github.com/ReactiveX/RxJava)).
- Repository pattern.
- View binding by [Butterknife](https://github.com/JakeWharton/butterknife)
- Image downloading/caching operations by [Picasso](http://square.github.io/picasso/)
   
[**Download apk**](https://drive.google.com/open?id=1fzRuO9Lz4fP-MGrCXbdsLmwjx1RA3WNs) if there are any build or configuration problem.