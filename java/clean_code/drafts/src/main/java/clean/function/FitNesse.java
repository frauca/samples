package clean.function;


public class FitNesse {

  /**
   * Example of functions
   */

  public static String testableHtml(
      PageData pageData,
      boolean includeSuiteSetup
  ) throws Exception {
    WikiPage wikiPage = pageData.getWikiPage();
    StringBuffer buffer = new StringBuffer();
    if (pageData.hasAttribute("Test")) {
      if (includeSuiteSetup) {
        WikiPage suiteSetup =
            PageCrawlerImpl.getInheritedPage(
                SuiteResponder.SUITE_SETUP_NAME, wikiPage
            );
        if (suiteSetup != null) {
          WikiPagePath pagePath =
              suiteSetup.getPageCrawler().getFullPath(suiteSetup);
          String pagePathName = PathParser.render(pagePath);
          buffer.append("!include -setup .")
              .append(pagePathName)
              .append("\n");
        }
      }
      WikiPage setup =
          PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
      if (setup != null) {
        WikiPagePath setupPath =
            wikiPage.getPageCrawler().getFullPath(setup);
        String setupPathName = PathParser.render(setupPath);
        buffer.append("!include -setup .")
            .append(setupPathName)
            .append("\n");
      }
    }
    buffer.append(pageData.getContent());
    if (pageData.hasAttribute("Test")) {
      WikiPage teardown =
          PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
      if (teardown != null) {
        WikiPagePath tearDownPath =
            wikiPage.getPageCrawler().getFullPath(teardown);
        String tearDownPathName = PathParser.render(tearDownPath);
        buffer.append("\n")
            .append("!include -teardown .")
            .append(tearDownPathName)
            .append("\n");
      }
    }


    /**
     *Try to reduce by myself in 3 minutes
     */

    public static String testableHtmlMyself(
        PageData pageData,
    boolean includeSuiteSetup
  ) throws Exception {
      WikiPage wikiPage = pageData.getWikiPage();
      StringBuffer buffer = new StringBuffer();
      if (pageData.hasAttribute("Test")) {
        if (includeSuiteSetup) {
          WikiPage suiteSetup =
              PageCrawlerImpl.getInheritedPage(
                  SuiteResponder.SUITE_SETUP_NAME, wikiPage
              );
          appendPagePath(suiteSetup,buffer)
        }
        WikiPage setup =
            PageCrawlerImpl.getInheritedPage("SetUp", wikiPage);
        appendPagePath(setup,buffer)
      }
      buffer.append(pageData.getContent());
      if (pageData.hasAttribute("Test")) {
        WikiPage teardown =
            PageCrawlerImpl.getInheritedPage("TearDown", wikiPage);
        appendPagePath(teardown,buffer)
      }

      public String getFullPagePath(WikiPage page){
        WikiPagePath pagePath =
            suiteSetup.getPageCrawler().getFullPath(page);
        return PathParser.render(pagePath);
      }

      public String getIncludeString(String path){
        return "!include -setup ." +path +"\n";
    }

      public void appendPagePath(WikiPage page,StringBuffer buffer){
        if(page!=null){
          String pagePath = getFullPagepath(page);
          buffer.append(getIncludeString(setupPath))
        }
      }

      }
    }

    /**
     *his solution
     */
    public static String renderPageWithSetupsAndTeardowns(
        PageData pageData, boolean isSuite
    ) throws Exception {
      boolean isTestPage = pageData.hasAttribute("Test");
      if (isTestPage) {
        WikiPage testPage = pageData.getWikiPage();
        StringBuffer newPageContent = new StringBuffer();
        includeSetupPages(testPage, newPageContent, isSuite);
        newPageContent.append(pageData.getContent());
        includeTeardownPages(testPage, newPageContent, isSuite);
        pageData.setContent(newPageContent.toString());
      }
      return pageData.getHtml();
    }
