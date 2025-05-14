package validators;

public class DoubleValidator {
    public boolean validate(String str, Double min, Double max) {
        return str.matches("-?(?:[0-9](?:[,.]\\d+|)|[1-9]\\d*[,.]?\\d+)") && Double.parseDouble(str.replace(",", ".")) >= min && Double.parseDouble(str.replace(",", ".")) <= max;
    }
}
