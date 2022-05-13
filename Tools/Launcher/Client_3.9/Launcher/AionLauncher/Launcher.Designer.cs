using System;
using System.Drawing;
using System.Drawing.Imaging;
namespace AionLauncher
{
    partial class Launcher
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        /// 
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Launcher));
            this.lblStatusText = new System.Windows.Forms.Label();
            this.lblServerStatus = new System.Windows.Forms.Label();
            this.news_panel = new System.Windows.Forms.Panel();
            this.pctLogo = new System.Windows.Forms.PictureBox();
            this.btnLaunch = new Glass.GlassButton();
            this.btnSettings = new Glass.GlassButton();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.label1 = new System.Windows.Forms.Label();
            this.progressBar1 = new System.Windows.Forms.ProgressBar();
            this.progressBar2 = new System.Windows.Forms.ProgressBar();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.timer2 = new System.Windows.Forms.Timer(this.components);
            this.timer3 = new System.Windows.Forms.Timer(this.components);
            this.timer4 = new System.Windows.Forms.Timer(this.components);
            this.timer5 = new System.Windows.Forms.Timer(this.components);
            this.btnMin = new Glass.GlassButton();
            this.btnExit = new Glass.GlassButton();
            this.appName = new System.Windows.Forms.Label();
            this.lblNews = new System.Windows.Forms.Panel();
            this.dragMain = new Microsoft.VisualBasic.PowerPacks.ShapeContainer();
            this.grap = new Microsoft.VisualBasic.PowerPacks.RectangleShape();
            this.BannerBrowser = new System.Windows.Forms.WebBrowser();
            this.NewsTimer = new System.Windows.Forms.Timer(this.components);
            this.CheckVersionLbl = new System.Windows.Forms.LinkLabel();
            this.Loading = new System.Windows.Forms.PictureBox();
            this.AutoStartTimer = new System.Timers.Timer();
            ((System.ComponentModel.ISupportInitialize)(this.pctLogo)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.Loading)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.AutoStartTimer)).BeginInit();
            this.SuspendLayout();
            // 
            // lblStatusText
            // 
            resources.ApplyResources(this.lblStatusText, "lblStatusText");
            this.lblStatusText.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(168)))), ((int)(((byte)(168)))), ((int)(((byte)(168)))));
            this.lblStatusText.ForeColor = System.Drawing.Color.Black;
            this.lblStatusText.Name = "lblStatusText";
            // 
            // lblServerStatus
            // 
            resources.ApplyResources(this.lblServerStatus, "lblServerStatus");
            this.lblServerStatus.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(168)))), ((int)(((byte)(168)))), ((int)(((byte)(168)))));
            this.lblServerStatus.ForeColor = System.Drawing.Color.Khaki;
            this.lblServerStatus.Name = "lblServerStatus";
            // 
            // news_panel
            // 
            this.news_panel.BackColor = System.Drawing.Color.Transparent;
            resources.ApplyResources(this.news_panel, "news_panel");
            this.news_panel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.news_panel.ForeColor = System.Drawing.Color.Transparent;
            this.news_panel.Name = "news_panel";
            // 
            // pctLogo
            // 
            this.pctLogo.BackColor = System.Drawing.Color.Transparent;
            resources.ApplyResources(this.pctLogo, "pctLogo");
            this.pctLogo.Name = "pctLogo";
            this.pctLogo.TabStop = false;
            // 
            // btnLaunch
            // 
            this.btnLaunch.BackColor = System.Drawing.Color.MediumBlue;
            resources.ApplyResources(this.btnLaunch, "btnLaunch");
            this.btnLaunch.ForeColor = System.Drawing.Color.Lavender;
            this.btnLaunch.GlowColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(192)))), ((int)(((byte)(255)))));
            this.btnLaunch.InnerBorderColor = System.Drawing.Color.Transparent;
            this.btnLaunch.Name = "btnLaunch";
            this.btnLaunch.UseCompatibleTextRendering = true;
            this.btnLaunch.Click += new System.EventHandler(this.btnLaunch_Click);
            // 
            // btnSettings
            // 
            this.btnSettings.BackColor = System.Drawing.Color.Silver;
            resources.ApplyResources(this.btnSettings, "btnSettings");
            this.btnSettings.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(90)))), ((int)(((byte)(90)))), ((int)(((byte)(90)))));
            this.btnSettings.GlowColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(184)))), ((int)(((byte)(184)))));
            this.btnSettings.InnerBorderColor = System.Drawing.Color.Transparent;
            this.btnSettings.Name = "btnSettings";
            this.btnSettings.OuterBorderColor = System.Drawing.Color.Silver;
            this.btnSettings.ShineColor = System.Drawing.Color.Transparent;
            this.btnSettings.TabStop = false;
            this.btnSettings.Click += new System.EventHandler(this.btnSettings_Click);
            // 
            // pictureBox1
            // 
            this.pictureBox1.BackColor = System.Drawing.Color.Transparent;
            resources.ApplyResources(this.pictureBox1, "pictureBox1");
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.TabStop = false;
            // 
            // label1
            // 
            resources.ApplyResources(this.label1, "label1");
            this.label1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(168)))), ((int)(((byte)(168)))), ((int)(((byte)(168)))));
            this.label1.ForeColor = System.Drawing.Color.Black;
            this.label1.Name = "label1";
            // 
            // progressBar1
            // 
            resources.ApplyResources(this.progressBar1, "progressBar1");
            this.progressBar1.Name = "progressBar1";
            // 
            // progressBar2
            // 
            resources.ApplyResources(this.progressBar2, "progressBar2");
            this.progressBar2.Name = "progressBar2";
            // 
            // label2
            // 
            resources.ApplyResources(this.label2, "label2");
            this.label2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(168)))), ((int)(((byte)(168)))), ((int)(((byte)(168)))));
            this.label2.ForeColor = System.Drawing.Color.Black;
            this.label2.Name = "label2";
            // 
            // label3
            // 
            resources.ApplyResources(this.label3, "label3");
            this.label3.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(168)))), ((int)(((byte)(168)))), ((int)(((byte)(168)))));
            this.label3.ForeColor = System.Drawing.Color.Black;
            this.label3.Name = "label3";
            // 
            // timer1
            // 
            this.timer1.Interval = 500;
            this.timer1.Tag = "";
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // timer2
            // 
            this.timer2.Interval = 1000;
            this.timer2.Tag = "";
            this.timer2.Tick += new System.EventHandler(this.timer2_Tick);
            // 
            // timer3
            // 
            this.timer3.Interval = 500;
            this.timer3.Tag = "";
            this.timer3.Tick += new System.EventHandler(this.timer3_Tick);
            // 
            // timer4
            // 
            this.timer4.Interval = 1000;
            this.timer4.Tag = "";
            this.timer4.Tick += new System.EventHandler(this.timer4_Tick);
            // 
            // timer5
            // 
            this.timer5.Interval = 500;
            this.timer5.Tag = "";
            this.timer5.Tick += new System.EventHandler(this.timer5_Tick);
            // 
            // btnMin
            // 
            this.btnMin.BackColor = System.Drawing.Color.Silver;
            resources.ApplyResources(this.btnMin, "btnMin");
            this.btnMin.ForeColor = System.Drawing.Color.Transparent;
            this.btnMin.GlowColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(184)))), ((int)(((byte)(184)))));
            this.btnMin.Image = global::AionLauncher.Properties.Resources.minimize;
            this.btnMin.InnerBorderColor = System.Drawing.Color.Transparent;
            this.btnMin.Name = "btnMin";
            this.btnMin.OuterBorderColor = System.Drawing.Color.Silver;
            this.btnMin.ShineColor = System.Drawing.Color.Transparent;
            this.btnMin.TabStop = false;
            this.btnMin.Click += new System.EventHandler(this.btnMin_Click);
            // 
            // btnExit
            // 
            this.btnExit.BackColor = System.Drawing.Color.Silver;
            resources.ApplyResources(this.btnExit, "btnExit");
            this.btnExit.ForeColor = System.Drawing.Color.Transparent;
            this.btnExit.GlowColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(184)))), ((int)(((byte)(184)))));
            this.btnExit.Image = global::AionLauncher.Properties.Resources.close;
            this.btnExit.InnerBorderColor = System.Drawing.Color.Transparent;
            this.btnExit.Name = "btnExit";
            this.btnExit.OuterBorderColor = System.Drawing.Color.Silver;
            this.btnExit.ShineColor = System.Drawing.Color.Transparent;
            this.btnExit.TabStop = false;
            this.btnExit.Click += new System.EventHandler(this.btnExit_Click);
            // 
            // appName
            // 
            resources.ApplyResources(this.appName, "appName");
            this.appName.BackColor = System.Drawing.Color.Transparent;
            this.appName.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(90)))), ((int)(((byte)(90)))), ((int)(((byte)(90)))));
            this.appName.Name = "appName";
            // 
            // lblNews
            // 
            this.lblNews.BackColor = System.Drawing.SystemColors.Window;
            resources.ApplyResources(this.lblNews, "lblNews");
            this.lblNews.Name = "lblNews";
            // 
            // dragMain
            // 
            resources.ApplyResources(this.dragMain, "dragMain");
            this.dragMain.Name = "dragMain";
            this.dragMain.Shapes.AddRange(new Microsoft.VisualBasic.PowerPacks.Shape[] {
            this.grap});
            this.dragMain.TabStop = false;
            // 
            // grap
            // 
            this.grap.BackColor = System.Drawing.Color.Transparent;
            resources.ApplyResources(this.grap, "grap");
            this.grap.Name = "grap";
            this.grap.MouseDown += new System.Windows.Forms.MouseEventHandler(this.drag_MouseDown);
            // 
            // BannerBrowser
            // 
            this.BannerBrowser.CausesValidation = false;
            this.BannerBrowser.IsWebBrowserContextMenuEnabled = false;
            resources.ApplyResources(this.BannerBrowser, "BannerBrowser");
            this.BannerBrowser.Name = "BannerBrowser";
            this.BannerBrowser.ScrollBarsEnabled = false;
            this.BannerBrowser.TabStop = false;
            this.BannerBrowser.WebBrowserShortcutsEnabled = false;
            this.BannerBrowser.NewWindow += new System.ComponentModel.CancelEventHandler(this.BannerBrowser_NewWindow);
            // 
            // NewsTimer
            // 
            this.NewsTimer.Enabled = true;
            this.NewsTimer.Interval = 2000;
            this.NewsTimer.Tick += new System.EventHandler(this.news_Tick);
            // 
            // CheckVersionLbl
            // 
            this.CheckVersionLbl.ActiveLinkColor = System.Drawing.Color.Transparent;
            resources.ApplyResources(this.CheckVersionLbl, "CheckVersionLbl");
            this.CheckVersionLbl.BackColor = System.Drawing.Color.Transparent;
            this.CheckVersionLbl.LinkBehavior = System.Windows.Forms.LinkBehavior.NeverUnderline;
            this.CheckVersionLbl.LinkColor = System.Drawing.Color.FromArgb(((int)(((byte)(90)))), ((int)(((byte)(90)))), ((int)(((byte)(90)))));
            this.CheckVersionLbl.Name = "CheckVersionLbl";
            this.CheckVersionLbl.TabStop = true;
            this.CheckVersionLbl.VisitedLinkColor = System.Drawing.Color.Black;
            this.CheckVersionLbl.Click += new System.EventHandler(this.CheckVersionLbl_Click);
            // 
            // Loading
            // 
            this.Loading.BackColor = System.Drawing.Color.Transparent;
            resources.ApplyResources(this.Loading, "Loading");
            this.Loading.Image = global::AionLauncher.Properties.Resources.loading;
            this.Loading.Name = "Loading";
            this.Loading.TabStop = false;
            // 
            // AutoStartTimer
            // 
            this.AutoStartTimer.Interval = 5000D;
            this.AutoStartTimer.SynchronizingObject = this;
            this.AutoStartTimer.Elapsed += new System.Timers.ElapsedEventHandler(this.AutoStartTimer_Tick);
            // 
            // Launcher
            // 
            this.AcceptButton = this.btnLaunch;
            resources.ApplyResources(this, "$this");
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Control;
            this.Controls.Add(this.Loading);
            this.Controls.Add(this.CheckVersionLbl);
            this.Controls.Add(this.BannerBrowser);
            this.Controls.Add(this.btnSettings);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.progressBar2);
            this.Controls.Add(this.progressBar1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.lblStatusText);
            this.Controls.Add(this.btnLaunch);
            this.Controls.Add(this.lblServerStatus);
            this.Controls.Add(this.pictureBox1);
            this.Controls.Add(this.appName);
            this.Controls.Add(this.btnExit);
            this.Controls.Add(this.btnMin);
            this.Controls.Add(this.pctLogo);
            this.Controls.Add(this.news_panel);
            this.Controls.Add(this.dragMain);
            this.DoubleBuffered = true;
            this.ForeColor = System.Drawing.Color.Transparent;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.MaximizeBox = false;
            this.Name = "Launcher";
            this.Load += new System.EventHandler(this.Launcher_Load);
            ((System.ComponentModel.ISupportInitialize)(this.pctLogo)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.Loading)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.AutoStartTimer)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private Glass.GlassButton btnSettings;
        private System.Windows.Forms.Label lblStatusText;
        private System.Windows.Forms.Label lblServerStatus;
        private System.Windows.Forms.Panel news_panel;
        private System.Windows.Forms.PictureBox pctLogo;
        private Glass.GlassButton btnLaunch;
        private Glass.GlassButton btnMin;
        private Glass.GlassButton btnExit;
        private System.Windows.Forms.Label appName;
        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ProgressBar progressBar1;
        private System.Windows.Forms.ProgressBar progressBar2;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Timer timer2;
        private System.Windows.Forms.Timer timer3;
        private System.Windows.Forms.Timer timer4;
        private System.Windows.Forms.Timer timer5;
        private System.Windows.Forms.Timer timer1;
        private Microsoft.VisualBasic.PowerPacks.ShapeContainer dragMain;
        private Microsoft.VisualBasic.PowerPacks.RectangleShape grap;
        private System.Windows.Forms.Panel lblNews;
        private System.Windows.Forms.WebBrowser BannerBrowser;
        private System.Windows.Forms.Timer NewsTimer;
        private System.Windows.Forms.LinkLabel CheckVersionLbl;
        private System.Windows.Forms.PictureBox Loading;
        private System.Timers.Timer AutoStartTimer;
    }
}

