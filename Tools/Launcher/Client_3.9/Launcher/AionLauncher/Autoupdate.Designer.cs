namespace AionLauncher
{
    partial class Autoupdate
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
        private void InitializeComponent()
        {
            this.progressBar1 = new System.Windows.Forms.ProgressBar();
            this.copy = new System.Windows.Forms.Label();
            this.percent = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // progressBar1
            // 
            this.progressBar1.BackColor = System.Drawing.SystemColors.MenuBar;
            this.progressBar1.ForeColor = System.Drawing.Color.Transparent;
            this.progressBar1.Location = new System.Drawing.Point(98, 153);
            this.progressBar1.Name = "progressBar1";
            this.progressBar1.Size = new System.Drawing.Size(271, 17);
            this.progressBar1.Step = 1;
            this.progressBar1.Style = System.Windows.Forms.ProgressBarStyle.Continuous;
            this.progressBar1.TabIndex = 0;
            // 
            // copy
            // 
            this.copy.AutoSize = true;
            this.copy.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(92)))), ((int)(((byte)(45)))), ((int)(((byte)(48)))));
            this.copy.ForeColor = System.Drawing.Color.Tan;
            this.copy.Location = new System.Drawing.Point(185, 155);
            this.copy.Name = "copy";
            this.copy.Size = new System.Drawing.Size(110, 17);
            this.copy.TabIndex = 1;
            this.copy.Text = "U3J community 2013";
            this.copy.UseCompatibleTextRendering = true;
            // 
            // percent
            // 
            this.percent.AutoSize = true;
            this.percent.BackColor = System.Drawing.Color.Transparent;
            this.percent.ForeColor = System.Drawing.Color.White;
            this.percent.Location = new System.Drawing.Point(395, 155);
            this.percent.Name = "percent";
            this.percent.Size = new System.Drawing.Size(21, 13);
            this.percent.TabIndex = 2;
            this.percent.Text = "0%";
            // 
            // Autoupdate
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackgroundImage = global::AionLauncher.Properties.Resources.Splah01;
            this.ClientSize = new System.Drawing.Size(468, 216);
            this.Controls.Add(this.percent);
            this.Controls.Add(this.progressBar1);
            this.Controls.Add(this.copy);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "Autoupdate";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.Manual;
            this.Text = "Updater";
            this.TopMost = true;
            this.Load += new System.EventHandler(this.Autoupdate_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ProgressBar progressBar1;
        private System.Windows.Forms.Label copy;
        private System.Windows.Forms.Label percent;
    }
}