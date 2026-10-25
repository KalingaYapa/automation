# Alignment with the supplied team sample

The user confirmed that this change is structural: existing test behaviours are preserved. No Accept Change or Code View scenario was added.

## What changed

| Supplied pattern | Implementation |
| --- | --- |
| `Para1TestCase` / `ParaPageObjects1` | Paragraph cases now use this pair in the `para` package. |
| `@Test(enabled = true, groups = { "Para", "P2-Para" })` | All 12 paragraph cases declare these values explicitly, with their existing framework ID/description. |
| Login/search/Edit inside each test | Each editor/paragraph method logs and calls `ReusableMethods.loginAndSearchFileClickEdit()` after its start message. Navigation is no longer hidden in a subclass `@BeforeMethod`. |
| Logged page-object initialization | Each test explicitly creates its page object after navigation. |
| Explicit frame changes in the test | Test bodies log `getDriver().switchTo().frame(p.getEditorFrame())` and `defaultContent()` around VE versus toolbar/DS operations. Page Objects do not silently switch frames. |
| XPath `@FindBy` fields | Fixed editor, paragraph, toolbar and navigation elements are declared with `@FindBy(xpath = ...)`. Runtime section scoping uses relative `By.xpath` where needed. |
| Small Page Object actions/checks | XPath fields feed `elementClick`, `is...` checks and `get...` reads. Technical caret/style readers are reused without moving scenario assertions into the Page Object. |
| Visible action/assertion/log sequence | Each method shows its actions, state reads, assertions and cleanup. The six paragraph formatting cases no longer delegate their whole scenario to a private test helper. |

## Deliberate details

- The iframe XPath identifies CKEditor's visible editing frame. Tests use that WebElement instead of assuming frame index zero.
- XPath uses stable IDs, class tokens and semantic `data-element` attributes. Generated `inno_ref` values such as 55/56 are not hardcoded. Section references are captured at runtime only to scope existing paragraph checks.
- Enter cases select the paragraph in VE before taking the DS baseline, matching the supplied preparation order and allowing the tree to update from the actual selection.
- Enter is sent while the editor frame is active. The test then switches to default content to inspect Document Structure. This keeps the key action attached to its verified caret location.
- An observed value is captured once, logged and asserted. The sample's second WebDriver read solely for logging is avoided.
- Loggers are class-specific `private static final Logger logger`; repeated step text uses named constants. Log4j2 uses placeholders rather than concatenating log strings.
- Cleanup remains in `finally`, with a successful finish logged afterward. Existing formatting/text/count assertions and Undo cleanup remain intact.

## Case ownership

| Class | Cases |
| --- | --- |
| `documenteditor.DocumentEditor1TestCase` | 21 title-bar, existing Bold, heading-formatting and Selected Element cases: TC_01–TC_18 and TC_25–TC_27. |
| `para.Para1TestCase` | 12 paragraph cases: TC_19 and TC_28–TC_38. |
| `portal.Portal1TestCase` | 5 navigation cases: TC_20–TC_24; its prerequisite steps are also explicit in each test. |

All 38 IDs and automation method names are retained. Default suites select all three classes. `DocumentEditor.xml` still selects all 33 editor-related cases across DocumentEditor and Para. `ParagraphTests.xml` selects the 12 Para cases. `FormattingTests.xml` retains its original five methods across the two feature classes.

```bash
mvn clean test
mvn clean test -DsuiteXmlFile=ParagraphTests.xml
mvn clean test -Dgroups=P2-Para
```

## Readiness checks

Shared navigation requires a displayed XPath-selected editor frame with a ready,
connected CKEditor document. The frame getter chooses a displayed match rather
than the first potentially hidden iframe. A local fixture verified a delayed
visible editor alongside a ready hidden editor, plus rejection of hidden-only
editors. This is an atomic readiness check, not a test retry or page refresh.

Document Structure checks remain strict: they wait for the selected paragraph's
section and never treat an empty tree as a zero-count success.

## Validation — 25 October 2026

- Compilation and the naming/grouping/format checker passed. The checker verifies explicit enabled=true, per-test navigation/construction, XPath fields, no Page Object frame switches and suite method ownership.
- Two full runs exercised all 38 methods. All **26 editor/portal cases passed**. The first run identified an unavailable frame; the second identified an empty DS before selection in the Redo case. Neither failure was skipped or hidden.
- After the visibility/readiness correction and sample-aligned paragraph selection order, the final **12-case paragraph suite passed with 0 failures, errors or skips**.
- The two list cases passed an additional focused check with their original non-list precondition explicitly retained.
- Together the passing scopes verify all **38 unique existing cases** (21 editor + 12 paragraph + 5 portal). There was no additional full-suite run after the final paragraph-only preparation change.
- The local hidden/delayed-frame fixture passed all three checks. No test retries, page refresh workaround, Save, Accept Change or Code View behaviour was added.
- Credential values were absent from output and temporary generated credentials were restored.
- SonarQube analysis was not run; no Quality Gate result is claimed.
