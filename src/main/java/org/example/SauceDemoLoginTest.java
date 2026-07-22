package org.example;

import com.microsoft.playwright.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SauceDemoLoginTest {
    public static void main(String[] args) {
        // 1. 创建 Playwright 实例
        try (Playwright playwright = Playwright.create()) {

            // 2. 启动浏览器：Chromium（headless=false 显示浏览器窗口；true 无头模式）
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
                    .setSlowMo(800) // 执行延迟，方便肉眼观察，正式测试可删掉
            );

            // 3. 创建上下文（隔离会话、Cookie）
            BrowserContext context = browser.newContext();

            // 4. 打开页面
            Page page = context.newPage();

            try {
                // 访问地址
                page.navigate("https://www.saucedemo.com/");

                // 输入用户名
                //page.locator("#user-name").fill("standard_user");

                // CSS 属性选择器
                page.locator("input[name='user-name']").fill("standard_user");
                //page.locator("input[placeholder='Username']");

                // Playwright语义化API（底层同样封装了定位逻辑，更推荐）
                //page.getByPlaceholder("Username");
                //page.getByTestId("username");

                // 输入密码
                page.locator("#password").fill("secret_sauce");

                // 点击登录按钮
                page.locator("#login-button").click();

                // ======= 断言（重点！自动化核心） =======
                // 断言商品页面标题可见，代表登录成功
                assertThat(page.locator(".title")).hasText("Products");

                System.out.println("✅ 登录流程执行成功！");

            } finally {
                // 资源释放
                page.close();
                context.close();
                browser.close();
            }
        }
    }
}