<!-- Top bar with logo/title, search, login/register -->
<div class="container-fluid py-3 border-bottom">
  <div class="row align-items-center">
    <!-- Left: Logo / Title -->
    <div class="col-md-3 d-flex align-items-center">
      <a href="<%= request.getContextPath() %>/">
        <img src="<%= request.getContextPath() %>/assets/images/logo.png"
             alt="Ebook Logo"
             style="height:50px; width:auto; margin-right:10px;">
      </a>
      
    </div>

    <!-- Middle: Search -->
    <div class="col-md-6">
      <form class="form-inline d-flex">
        <input class="form-control mr-2 flex-grow-1" type="search"
               placeholder="Search" aria-label="Search">
        <button class="btn btn-primary my-2 my-sm-0" type="submit">Search</button>
      </form>
    </div>

    <!-- Right: Login & Register -->
    <div class="col-md-3 text-md-right mt-2 mt-md-0">
      <a href="#" class="btn btn-success mr-2">Login</a>
      <a href="#" class="btn btn-primary">Register</a>
    </div>
  </div>
</div>


<nav class="navbar navbar-expand-lg navbar-light bg-light">


  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item active">
        <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">New Books</a>
      </li>
      
      <li class="nav-item">
        <a class="nav-link" href="#">Recent Books</a>
      </li>
     
      <li class="nav-item">
        <a class="nav-link disabled" href="#">Recommended Books</a>
      </li>
    </ul>
    <form class="form-inline my-2 my-lg-0">
      <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>
</nav>