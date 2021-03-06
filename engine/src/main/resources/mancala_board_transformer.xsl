<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:param name="num_stones" />

    <xsl:template name="position">
        <xsl:attribute name="minWidth">-Infinity</xsl:attribute>
        <xsl:attribute name="minHeight">-Infinity</xsl:attribute>
        <xsl:attribute name="GridPane.columnIndex">
            <xsl:choose>
                <xsl:when test="@column">
                    <xsl:value-of select="@column" />
                </xsl:when>
                <xsl:otherwise>1</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name="GridPane.rowIndex">
            <xsl:choose>
                <xsl:when test="@row">
                    <xsl:value-of select="@row" />
                </xsl:when>
                <xsl:otherwise>1</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name="GridPane.columnSpan">
            <xsl:choose>
                <xsl:when test="@columnspan">
                    <xsl:value-of select="@columnspan" />
                </xsl:when>
                <xsl:otherwise>1</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name="GridPane.rowSpan">
            <xsl:choose>
                <xsl:when test="@rowspan">
                    <xsl:value-of select="@rowspan" />
                </xsl:when>
                <xsl:otherwise>1</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="player-depot">
        <BorderPane xmlns:fx="http://javafx.com/fxml">
            <xsl:attribute name="id">
                <xsl:value-of select="@id" />
            </xsl:attribute>

            <xsl:call-template name="position" />


            <xsl:if test="@player = 0">
                <right>
                    <Label text="Player 1" styleClass="player-label" fx:id="depotLabel0" BorderPane.alignment="CENTER">
                        <padding><Insets left="10"/></padding>
                    </Label>
                </right>
            </xsl:if>
            <xsl:if test="@player = 1">
                <left>
                    <Label text="Player 2" styleClass="player-label" fx:id="depotLabel1" BorderPane.alignment="CENTER">
                        <padding><Insets right="10"/></padding>
                    </Label>
                </left>
            </xsl:if>

            <center>
                <Button disable="true">
                    <xsl:attribute name="text">0</xsl:attribute>
                </Button>
            </center>
        </BorderPane>
    </xsl:template>

    <xsl:template match="slot">
        <Button onAction="#handleAction">
            <xsl:attribute name="id">
                <xsl:value-of select="@id" />
            </xsl:attribute>

            <xsl:attribute name="text">
                <xsl:value-of select="$num_stones" />
            </xsl:attribute>

            <xsl:call-template name="position" />
        </Button>
    </xsl:template>

    <xsl:template match="board">
        <AnchorPane xmlns="http://javafx.com/javafx"
                    xmlns:fx="http://javafx.com/fxml"
                    fx:id="root"
                    AnchorPane.topAnchor="0.0"
                    AnchorPane.bottomAnchor="0.0"
                    AnchorPane.leftAnchor="0.0"
                    AnchorPane.rightAnchor="0.0"
                    fx:controller="at.pwd.boardgame.controller.BoardController">
            <GridPane alignment="center"
                      hgap="10"
                      vgap="10"
                      prefHeight="400.0"
                      prefWidth="600.0"
                      AnchorPane.topAnchor="0.0"
                      AnchorPane.bottomAnchor="0.0"
                      AnchorPane.leftAnchor="0.0"
                      AnchorPane.rightAnchor="0.0"
                      fx:id="grid">
                <xsl:apply-templates />
            </GridPane>

            <Label fx:id="turnCounter"
                   AnchorPane.bottomAnchor="10.0"
                   AnchorPane.rightAnchor="10.0" />
            <stylesheets>
                <URL value="@style.css" />
            </stylesheets>
        </AnchorPane>
    </xsl:template>

    <xsl:template match="/">
        <xsl:processing-instruction name="import">
            java.lang.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            java.util.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            javafx.scene.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            javafx.scene.layout.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            javafx.scene.control.*
        </xsl:processing-instruction>

        <xsl:processing-instruction name="import">
            java.net.*
        </xsl:processing-instruction>
        <xsl:processing-instruction name="import">
            javafx.geometry.Insets
        </xsl:processing-instruction>

        <xsl:apply-templates />
    </xsl:template>

</xsl:stylesheet>