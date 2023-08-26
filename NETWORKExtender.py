class NETWORKExtender:
    def __init__(self, isp_signal_strength):
        self.isp_signal_strength = isp_signal_strength
    
    def amplify_signal(self, amplification_factor):
        amplified_signal = self.isp_signal_strength * amplification_factor
        return amplified_signal

# Assuming ISP signal strength is 10 and amplification factor is 2.5
isp_signal_strength = 10
amplification_factor = 2.5

extender = WiFiExtender(isp_signal_strength)
amplified_signal = extender.amplify_signal(amplification_factor)

print(f"Original signal strength: {isp_signal_strength}")
print(f"Amplified signal strength: {amplified_signal}")