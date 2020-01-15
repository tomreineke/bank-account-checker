<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match = "/">
        export const transactions = [
        <xsl:for-each select="table/tbody/tr">
            {
            <xsl:for-each select="descendant::node()[starts-with(@class,'title')]">
                source: '<xsl:value-of select="."/>',
            </xsl:for-each>
            <xsl:for-each select="descendant::node()[contains(@class,'offscreen')]">
                amount: '<xsl:value-of select="."/>'
            </xsl:for-each>
            },
        </xsl:for-each>
        ];
    </xsl:template>
</xsl:stylesheet>
