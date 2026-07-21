package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

public class Main {
    public static void main(String[] args) {
        // 搜索关键词统一定义，方便复用断言
        String searchKeyword = "playwright java";
        try (Playwright playwright = Playwright.create()) {
            BrowserType chromium = playwright.chromium();
            Browser browser = chromium.launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setSlowMo(500)
            );

            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            // 全局缩短超时，不用等满30秒
            page.setDefaultTimeout(10000);

            // 1. 打开百度，断言页面标题
            page.navigate("https://www.baidu.com");
            PlaywrightAssertions.assertThat(page).hasTitle("百度一下，你就知道");
            System.out.println("【断言通过】页面标题校验");

            // 2. 断言搜索框可见
            PlaywrightAssertions.assertThat(page.locator("#kw")).isVisible();
            System.out.println("【断言通过】搜索框可见");

            // 3. 输入关键词，断言输入值正确
            page.locator("#kw").fill(searchKeyword);
            PlaywrightAssertions.assertThat(page.locator("#kw")).hasValue(searchKeyword);
            System.out.println("【断言通过】输入框内容匹配");

            // 4. 点击搜索按钮，断言按钮可用
            page.locator("#su").click();
            PlaywrightAssertions.assertThat(page.locator("#su")).isEnabled();
            System.out.println("【断言通过】搜索按钮可用");

            // ====================== 新增：搜索结果关键词断言 ======================
            // #content_left 百度所有搜索结果容器
            // containsText：模糊匹配，只要结果里包含关键词就通过，内置自动等待
            PlaywrightAssertions.assertThat(page.locator("#content_left"))
                    .containsText(searchKeyword);
            System.out.println("【断言通过】搜索结果包含关键词：" + searchKeyword);
            // ===================================================================

            System.out.println("==== 全部自动化用例执行成功 ====");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("自动化用例执行失败：断言/页面操作异常");
            // 非0退出码，Maven输出BUILD FAILURE
            System.exit(1);
        }
    }
}