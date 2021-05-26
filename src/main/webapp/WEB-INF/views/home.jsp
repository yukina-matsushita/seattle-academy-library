<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=utf8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>ホーム｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
            <nav id="menubar">
                <ul class="menu2">
                    <li><a href="<%=request.getContextPath()%>/addBook">書籍の追加</a></li>
                    <li><a href="<%=request.getContextPath()%>/bulkRegistration">一括登録</a></li> 
                </ul>
            </nav>
            <img class="main" src="resources/img/mainBackimg.jpg" />

        <h1>Home</h1>

        <div>
            <form method="post" action="<%=request.getContextPath()%>/search">
                <label class="label2">タイトル検索ができます</label> 
                <input type="text" class="input" id="search1" name="searchWord" required> 
                <input type="submit" name="search_box" value="検索">

            </form>
        </div>
        <div class="content_body">
            <c:if test="${!empty resultMessage}">
                <div class="error_msg">${resultMessage}</div>
            </c:if>
            <c:if test="${!empty searchBookList}">
                <div class="searchbooklist">
                    <c:forEach var="bookInfo" items="${searchBookList}">
                        <div class="books">
                            <form method="post" class="book_thumnail" action="<%=request.getContextPath()%>/details">
                                <a href="javascript:void(0)" onclick="this.parentNode.submit();"> <c:if test="${bookInfo.thumbnail=='null'}">
                                        <img class="book_noimg" src="resources/img/noImg.png">
                                    </c:if> <c:if test="${bookInfo.thumbnail !='null'}">
                                        <img class="book_noimg" src="${bookInfo.thumbnail}">
                                    </c:if>
                                </a> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                            </form>
                            <ul>
                                <li class="book_title">${bookInfo.title}</li>
                                <li class="book_author">${bookInfo.author}</li>
                                <li class="book_publisher">出版社:${bookInfo.publisher}</li>
                                <li class="book_publishDate">出版日:${bookInfo.publishDate}</li>
                            </ul>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <div class="booklist">
                <c:forEach var="bookInfo" items="${bookList}">
                    <div class="books">
                        <form method="post" class="book_thumnail" action="<%=request.getContextPath()%>/details">
                            <a href="javascript:void(0)" onclick="this.parentNode.submit();"> <c:if test="${bookInfo.thumbnail=='null'}">
                                    <img class="book_noimg" src="resources/img/noImg.png">
                                </c:if> <c:if test="${bookInfo.thumbnail !='null'}">
                                    <img class="book_noimg" src="${bookInfo.thumbnail}">
                                </c:if>
                            </a> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                        </form>
                        <ul>
                            <li class="book_title">${bookInfo.title}</li>
                            <li class="book_author">${bookInfo.author}</li>
                            <li class="book_publisher">出版社:${bookInfo.publisher}</li>
                            <li class="book_publishDate">出版日:${bookInfo.publishDate}</li>
                        </ul>
                    </div>
                </c:forEach>
            </div>
        </div>
        </div>
    </main>
    <p class="nav-fix-pos-pagetop">
        <a href="#">↑</a>
    </p>
</body>
</html>
