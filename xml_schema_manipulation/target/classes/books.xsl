<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <head>
                <style>
                    body {
                        color:#FFFFFF;
                        font-family:'Open Sans',Verdana;
                        background-color:#3e3e3e;
                    }

                    h4 {
                        margin:0px;
                        float:left;
                        width:100%;
                        margin:5px;
                    }

                    .s-book {
                        height:180px;
                        width:20%;
                        float:left;
                        background-color:#101F2F;
                        border-radius:10px;
                        margin:5px;
                    }

                    .s-brown-tag {
                        height:30px;
                        line-height:30px;
                        width:50%;
                        background-color:#483415;
                        border-radius:0px 0px 10px 0px;
                        float:left;
                    }
                    .s-green-tag {
                        height:30px;
                        line-height:30px;
                        width:50%;
                        background-color:#0E3025;
                        border-radius:0px 0px 0px 10px;
                        float:left;
                    }

                    .s-book span {
                        float:left;
                        width:100%;
                        overflow:hidden;
                        margin-bottom:10px;
                    }

                </style>
            </head>
            <body>
                <h3>Books that look cooler now</h3>

                <xsl:for-each select="catalog/book">
                    <div class="s-book">
                        <h4><xsl:value-of select="title"></xsl:value-of></h4>
                        <span style="height:55px;"><xsl:value-of select="description"></xsl:value-of></span>
                        <span style="text-align:right;">
                            By <xsl:value-of select="author"></xsl:value-of>
                        </span>
                        <span>
                            Published on <xsl:value-of select="publish_date"></xsl:value-of>
                        </span>
                        <div class="s-green-tag">
                            <xsl:value-of select="price"></xsl:value-of>
                        </div>
                        <div class="s-brown-tag">
                            <xsl:value-of select="genre"></xsl:value-of>
                        </div>
                    </div>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>