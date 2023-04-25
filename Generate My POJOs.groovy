import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = ""
typeMapping = [
        (~/(?i)bigint/)            : "Long",
        (~/(?i)int|tinyint/)       : "Integer",
        (~/(?i)bool|bit/)          : "Boolean",
        (~/(?i)float|double|real/) : "Double",
        (~/(?i)decimal/)           : "BigDecimal",
        (~/(?i)datetime|timestamp/): "LocalDateTime",
        (~/(?i)date/)              : "LocalDate",
        (~/(?i)time/)              : "LocalTime",
        (~/(?i)/)                  : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable }.each { generate(it, dir) }
}

def generate(table, dir) {
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    packageName = getPackageName(dir)
    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, className + ".java")), "UTF-8"))
    printWriter.withPrintWriter { out -> generate(out, className, fields) }
}

def getPackageName(dir) {
    return dir.toString().replaceAll("\\\\", ".").replaceAll("/", ".").replaceAll("^.*src(\\.main\\.java\\.)?", "") + ";"
}

def generate(out, className, fields) {
    out.println "package $packageName"
    out.println ""
    out.println "import lombok.Data;"
    Set types = new HashSet<>()
    fields.each() {
        types.add(it.type)
    }
    if (types.contains("LocalDate")) {
        out.println "import java.time.LocalDate;"
    }
    if (types.contains("LocalTime")) {
        out.println "import java.time.LocalTime;"
    }
    if (types.contains("LocalDateTime")) {
        out.println "import java.time.LocalDateTime;"
    }
    if (types.contains("BigDecimal")) {
        out.println "import java.math.BigDecimal;"
    }
    out.println ""
    out.println "@Data"
    out.println "public class $className {"

    fields.each() {
        out.println ""
        if (isNotEmpty(it.comment)) {
            out.println "    // ${it.comment.toString()}";
        }
        out.println "    private ${it.type} ${it.name};"
    }
    out.println ""
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name : javaName(col.getName(), false),
                           type : typeStr,
                           colName : col.getName(),
                           comment : col.getComment(),
                           annos: ""]]
    }
}

def javaName(str, capitalize) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}

def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}