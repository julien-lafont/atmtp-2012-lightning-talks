package service

import java.text._

object StringUtil {

  def capFirst(o: Object) = {
    val str = o.toString
    if (str.length() == 0) str
    else (""+str.charAt(0)).toUpperCase + str.substring(1)
  }

  def noAccents(str: String) = {
    Normalizer
      .normalize(str, Normalizer.Form.NFKC)
      .replaceAll("[àáâãäåāąă]", "a")
      .replaceAll("[çćčĉċ]", "c")
      .replaceAll("[ďđð]", "d")
      .replaceAll("[èéêëēęěĕė]", "e")
      .replaceAll("[ƒſ]", "f")
      .replaceAll("[ĝğġģ]", "g")
      .replaceAll("[ĥħ]", "h")
      .replaceAll("[ìíîïīĩĭįı]", "i")
      .replaceAll("[ĳĵ]", "j")
      .replaceAll("[ķĸ]", "k")
      .replaceAll("[łľĺļŀ]", "l")
      .replaceAll("[ñńňņŉŋ]", "n")
      .replaceAll("[òóôõöøōőŏœ]", "o")
      .replaceAll("[Þþ]", "p")
      .replaceAll("[ŕřŗ]", "r")
      .replaceAll("[śšşŝș]", "s")
      .replaceAll("[ťţŧț]", "t")
      .replaceAll("[ùúûüūůűŭũų]", "u")
      .replaceAll("[ŵ]", "w")
      .replaceAll("[ýÿŷ]", "y")
      .replaceAll("[žżź]", "z")
      .replaceAll("[æ]", "ae")
      .replaceAll("[ÀÁÂÃÄÅĀĄĂ]", "A")
      .replaceAll("[ÇĆČĈĊ]", "C")
      .replaceAll("[ĎĐÐ]", "D")
      .replaceAll("[ÈÉÊËĒĘĚĔĖ]", "E")
      .replaceAll("[ĜĞĠĢ]", "G")
      .replaceAll("[ĤĦ]", "H")
      .replaceAll("[ÌÍÎÏĪĨĬĮİ]", "I")
      .replaceAll("[Ĵ]", "J")
      .replaceAll("[Ķ]", "K")
      .replaceAll("[ŁĽĹĻĿ]", "L")
      .replaceAll("[ÑŃŇŅŊ]", "N")
      .replaceAll("[ÒÓÔÕÖØŌŐŎ]", "O")
      .replaceAll("[ŔŘŖ]", "R")
      .replaceAll("[ŚŠŞŜȘ]", "S")
      .replaceAll("[ÙÚÛÜŪŮŰŬŨŲ]", "U")
      .replaceAll("[Ŵ]", "W")
      .replaceAll("[ÝŶŸ]", "Y")
      .replaceAll("[ŹŽŻ]", "Z")
      .replaceAll("[ß]", "ss");
  }
  
  def slugify(str: String) = {
    noAccents(str).replaceAll("([a-z])'s([^a-z])", "$1s$2")
      .replaceAll("[^\\w]", "-")
      .replaceAll("-{2,}", "-")
      .replaceAll("-+$", "")
      .replaceAll("^-+", "")
      .toLowerCase(); }
}